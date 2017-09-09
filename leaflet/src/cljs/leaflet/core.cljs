(ns leaflet.core
  (:require [reagent.core :as reagent]))

(enable-console-print!)

(defn home-html []
  [:div#mapdiv {:style {:height "100vh"
                        :width "100vw"}}])

(def tile-url
  "http://c.tile.stamen.com/watercolor/{z}/{x}/{y}.png")

(def places [
             [ "Birmingham"	52.486243	-1.890401 ]
             [ "Wolverhampton"	52.586973	-2.12882 ]
             [ "Walsall"	52.586214	-1.982919 ]
             [ "Sutton Coldfield"	52.570385	-1.824042 ]
             [ "Dudley"	52.512255	-2.081112 ]
             [ "West Brom"	52.509038	-1.963938 ]
             [ "Halesowen"	52.449845	-2.050526 ]
             [ "Stechford"	52.48396	-1.807598 ]
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
