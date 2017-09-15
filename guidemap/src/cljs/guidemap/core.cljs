(ns guidemap.core
    (:require [reagent.core :as reagent :refer [atom]]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
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

(def moarplaces [
                 [ "Handsworth"	52.514388	-1.939250 ]
                 [ "Sparkbrook"	52.458830	-1.871054 ]
                 [ "Edgbaston"	52.460801	-1.914995 ]
                 [ "Moseley"	52.446083	-1.884233 ]
                 [ "Selly Oak"	52.440881	-1.938541 ]
                 [ "Bearwood"	52.476815	-1.969899 ]
                 [ "Bournville"	52.424404	-1.933897 ]
                 [ "Shirley"	52.410447	-1.819373 ]
                 [ "Acocks Green"	52.445584	-1.830836 ]
                 [ "Winson Green"	52.498711	-1.936483 ]
                 [ "Ladywood"	52.477664	-1.925651 ]
                 [ "Aston" 52.495981 -1.885272 ]
                 [ "Small Heath" 52.473672 -1.855207 ]
                 [ "Smethwick" 52.49285 -1.968226 ]
                 [ "Saltley" 52.4919 -1.8577 ]
                 [ "Stechford" 52.482778 -1.806944 ]
                 [ "Northfield" 52.408, -1.963 ]
                 [ "Bartley Green" 52.43, -1.98	 ]
                 [ "Kings Norton" 52.4072, -1.9272 ]
                 [ "Tipton" 52.5259, -2.0751	 ]
                 [ "Kingstanding" 52.548, -1.873 ]
                 [ "Quinton" 52.46, -2 ]
                 [ "Hall Green" 52.434, -1.839	 ]
                 [ "Druid's Heath" 52.406296, -1.888328 ]
                 [ "Oldbury" 52.505, -2.0159 ]
                 [ "Yardley" 52.4709562, -1.8159436 ]
                 [ "Castle Bromwich" 52.5069138, -1.7860432 ]
                 [ "Nechells" 52.5023095, -1.8605038 ]
                 [ "Great Barr" 52.5593574, -1.9301365 ]
                 [ "Perry Barr" 52.5225941, -1.8956363 ]
                 [ "Hamstead" 52.5318001, -1.9248531 ]
                 [ "Alum Rock" 52.4870712, -1.8315298 ]
                 [ "Olton" 52.4403249, -1.8082099 ]
                 [ "Sheldon"  52.4533229, -1.7780160 ]
                 [ "Hodge Hill" 52.4981788, -1.8112796 ]
                 [ "Shard End" 52.4984453, -1.7709979 ]
                 [ "Marston Green" 52.4684641, -1.7516876 ]
                 [ "Darlaston" 52.5733189, -2.0355619 ]
                 [ "Cradley Heath" 52.4721362, -2.0789445 ]
                 [ "Kingswinford" 52.4967692, -2.1661721 ]
                 [ "Stourbridge" 52.4577615, -2.1474345 ]
                 [ "Brierley Hill"  52.4823185, -2.1237803 ]
                 [ "Gornal"  52.5287213, -2.1152124 ]
                 [ "Bilston"  52.5700229, -2.0822333 ]
                 [ "Wednesbury" 52.5521806, -2.0224441 ]
                 [ "Bloxwich" 52.6155386, -2.0049655 ]
                 [ "Willenhall" 52.5847624, -2.0534738 ]
                 [ "Aldridge" 52.6040230, -1.9172173 ]
                 [ "Rowley Regis" 52.4833326, -2.0674067 ]
                 [ "Sedgley" 52.5417011, -2.1210960 ]
                 [ "Frankley" 52.4081559, -2.0058659 ]
                 [ "Little Aston" 52.5959832, -1.8693023 ]
                 [ "Brownhills" 52.6472181, -1.9321351 ]
                 [ "Rubery" 52.3926619, -2.0166846 ]
                 [ "Wednesfield" 52.5998276, -2.0813004 ]
                 [ "Oxley" 52.6188088, -2.1315250 ]
                 [ "Tettenhall" 52.5986015, -2.1665631 ]
                 [ "Merryhill" 52.5696558, -2.1715493 ]
                 [ "Ettingshall"  52.5638883, -2.1046112 ]
                 [ "Water Orton"   52.5171385, -1.7399451 ]
                 [ "Castle Vale" 52.5212593, -1.7845494 ]
                 [ "Wylde Green" 52.5456280, -1.8314247 ]
    ])

(def evenmoarplaces [
                     [ "Jewellery Quarter"	52.485796	-1.910880 ]
                     [ "Stirchley"	52.429717	-1.921767 ]
                     ])

(def places [
             [ "Birmingham"	52.483056 -1.893611 ]
             [ "Wolverhampton"	52.586973	-2.12882 ]
             [ "Walsall"	52.586214	-1.982919 ]
             [ "Sutton Coldfield"	52.570385	-1.824042 ]
             [ "Dudley"	52.512255	-2.081112 ]
             [ "West Brom"	52.517664	-1.995159 ]
             [ "Halesowen"	52.449845	-2.050526 ]
             [ "Chelmsley Wood"	52.479201	-1.732631 ]
             [ "Longbridge"	52.388445	-1.977676 ]
             [ "Kings Heath"	52.432447	-1.893119 ]
             [ "Solihull"	52.411811	-1.77761 ]
             [ "Erdington" 52.521931 -1.844611 ]
             [ "Harborne" 52.459709 -1.961668 ]
  ])

(defn mount-tiles [leaflet]
  (.addTo (.tileLayer js/L tile-url
                    (clj->js {:attribution "From OpenStreetmap"
                              :maxZoom 18
                              }))
            leaflet))

(defn add-marker-to-this-leaflet-map [leafletmap]
  (fn add-marker [copytext long lat]
    (let [marker (.circleMarker js/L #js [long lat] #js { :radius 0 } )]
      (.openTooltip (.bindTooltip (.addTo marker leafletmap) copytext #js { :permanent true :direction "center" :className "tooltip"} ))))
  )

(defn zoomchange []
  (let [leaflet (:leaflet @!local)]
    (let [add (add-marker-to-this-leaflet-map leaflet)]
      (doseq [place moarplaces] (apply add place))
      )
  ))

(defn home-did-mount []
  (reset! !local {:leaflet (.setView (.map js/L "mapdiv" #js { :minZoom 10 }) #js [52.53107999999999 -1.9730885000000171] 11 )})
  (let [leaflet (:leaflet @!local)]
    (let [add (add-marker-to-this-leaflet-map leaflet)]
    (do (mount-tiles leaflet)
        (doseq [place places] (apply add place))
        (.on leaflet "zoomend" zoomchange)
        ))))


(defn home-page []
  (reagent/create-class {:reagent-render home-html
                         :component-did-mount home-did-mount}))

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
