(ns leaflet.core
  (:require [reagent.core :as reagent]))

(enable-console-print!)

(defn home-html []
  [:div#mapdiv {:style {:height "100vh"
                        :width "100vw"}}])

(def tile-url
  "http://c.tile.stamen.com/watercolor/{z}/{x}/{y}.png")

(def places [
  ["yozer" 52.53107999999999 -1.9730885000000171]
  ["bifh" 52.52107999999999 -1.9730885000000171]
  ])

(defn mount-tiles [leaflet]
  (.addTo (.tileLayer js/L tile-url
                    (clj->js {:attribution "From OpenStreetmap"
                              :maxZoom 18
                              }))
            leaflet))

(defn add-marker-to-this-leaflet-map [leafletmap]
  (fn add-marker [copytext long lat]
    (let [marker (.marker js/L #js [long lat])]
      (.openPopup (.bindPopup (.addTo marker leafletmap) copytext))))
  )

(defn home-did-mount []
  (let [leaflet (.setView (.map js/L "mapdiv") #js [52.53107999999999 -1.9730885000000171] 11)]
    (let [add (add-marker-to-this-leaflet-map leaflet)]
    (do (mount-tiles leaflet)
        (doseq [place places] (apply add place))
        ))))


(defn home []
  (reagent/create-class {:reagent-render home-html
                         :component-did-mount home-did-mount}))

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))
