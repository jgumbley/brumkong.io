(ns guidemap.core
    (:require [reagent.core :as reagent :refer [atom]]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [guidemap.places :as p]
              [cljsjs.leaflet]))

(enable-console-print!)

(def !local (atom {}))

(defn home-html []
  [:div#mapdiv {:style {:height "100vh"
                        :width "100vw"}}
   :div#logo
   :div#showme])

(def tile-url
  "http://c.tile.stamen.com/watercolor/{z}/{x}/{y}.png")


(defn mount-tiles [leaflet]
  (.addTo
   (.tileLayer
    js/L tile-url (clj->js {:attribution "From OpenStreetmap" :maxZoom 18 })) leaflet))

(defn add-marker-to-this-leaflet-map [leafletmap]
  (fn add-marker [copytext long lat]
    (let [marker (.circleMarker js/L #js [long lat] #js { :radius 0 } )]
      (.openTooltip (.bindTooltip (.addTo marker leafletmap) copytext #js { :permanent true :direction "center" :className "tooltip"} ))))
  )

(defn zoomchange []
  (let [leaflet (:leaflet @!local)
        zoomlevel (.getZoom leaflet)]
    (do
      (if (> zoomlevel 11) (add-edgbo leaflet))
      (if (<= zoomlevel 11) (rm-edgbo leaflet))
      (println zoomlevel)
      ))
  )

(defn init-moarplaces []
  (let [leaflet (:leaflet @!local)
        add (add-marker-to-this-leaflet-map leaflet)]
    (doseq [place p/moarplaces] (apply add place))
    ))

(defn init-overviewplaces [leaflet]
  (let [add (add-marker-to-this-leaflet-map leaflet)]
    (doseq [place p/places] (apply add place))
    ))
    
(def !edgbo (atom {}))

(defn init-edgbo [leaflet]
  [ "Edgbaston"	52.460801	-1.914995 ]
  (let [edgbo-marker (.circleMarker js/L #js [52.460801 -1.914995] #js { :radius 0 } )]
    (do
      (.openTooltip (.bindTooltip edgbo-marker "edgbo" #js { :permanent true :direction "center" :className "tooltip"} ))
      (reset! !edgbo {:edgbo edgbo-marker})
    )))

(defn add-edgbo [leaflet]
  (let [edgbo-marker (:edgbo @!edgbo)]
    (do
      (.addLayer leaflet edgbo-marker)
      (println "edgbo on")
    )))

(defn rm-edgbo [leaflet]
  (let [edgbo-marker (:edgbo @!edgbo)]
    (do
      (.removeLayer leaflet edgbo-marker)
      (println "edgbo on")
      )))

(defn initialise-map []
  (reset! !local {:leaflet (.setView (.map js/L "mapdiv" #js { :minZoom 10 }) #js [52.53107999999999 -1.9730885000000171] 11 )})
  (let [leaflet (:leaflet @!local)]
    (do
      (mount-tiles leaflet)
      (init-overviewplaces leaflet)
      (init-edgbo leaflet)
      (.on leaflet "zoomend" zoomchange)
      )))

(defn home-page []
  (reagent/create-class {:reagent-render home-html
                         :component-did-mount initialise-map}))

;; -------------------------
;; Views

(defn about-page []
  [:div [:h2 "About guidemap"]
   [:div [:a {:href "/"} "go to the home page"]]])

;; -------------------------
;; Routes

(def page (atom #'home-page))

(defn current-page []
  [:div [@page]])

(secretary/defroute "/" []
  (reset! page #'home-page))

(secretary/defroute "/about" []
  (reset! page #'about-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
