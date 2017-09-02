(ns leaflet.core
    (:require [reagent.core :as reagent]))

(defn home-html []
  [:div#mapdiv {:style {:height "360px"}}])

(defn home-did-mount []
  (let [leaflet (.setView (.map js/L "mapdiv") #js [51.505 -0.09] 13)]
    (.addTo (.tileLayer js/L "https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}"
                        (clj->js {:attribution "Map data &copy; [...]"
                                  :maxZoom 18
                                  :id "mapbox.streets"
                                  :accessToken " "}))
            leaflet)))

(defn home []
  (reagent/create-class {:reagent-render home-html
                         :component-did-mount home-did-mount}))

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app"))
                  )
