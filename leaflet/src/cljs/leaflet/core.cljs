(ns leaflet.core
    (:require [reagent.core :as reagent]))

(defn home-html []
  [:div#mapdiv {:style {:height "100vh"
                        :width "100vw"}}])

(def tile-url
  "http://c.tile.stamen.com/watercolor/{z}/{x}/{y}.png")

(defn mount-tiles [leaflet]
  (.addTo (.tileLayer js/L tile-url
                    (clj->js {:attribution "From OpenStreetmap"
                              :maxZoom 18
                              }))
            leaflet))

(defn mount-pointer [leaflet]
  (.addTo (.marker js/L #js [52.53107999999999 -1.9730885000000171]) leaflet)
  )




(defn tooltip-market [copytext long lat leafletmap ]
  (let [marker (.marker js/L #js [long lat])]
  (.openPopup (.bindPopup (.addTo marker leafletmap) copytext))))

(defn home-did-mount []
  (let [leaflet (.setView (.map js/L "mapdiv") #js [52.53107999999999 -1.9730885000000171] 11)]
    (do (mount-tiles leaflet)
        (tooltip-market "yozer" 52.53107999999999 -1.9730885000000171 leaflet)
        (tooltip-market "nozer" 51.53107999999999 -1.9730885000000171 leaflet)
        )))


(defn home []
  (reagent/create-class {:reagent-render home-html
                         :component-did-mount home-did-mount}))

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))
