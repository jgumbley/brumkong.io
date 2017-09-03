(ns leaflet.core
    (:require [reagent.core :as reagent]))

(defn home-html []
  [:div#mapdiv {:style {:height "100vh"
                        :width "100vw"}}])

(defn home-did-mount []
  (let [leaflet (.setView (.map js/L "mapdiv") #js [52.53107999999999 -1.9730885000000171] 11)]
    (.addTo (.tileLayer js/L "http://{s}.tile2.opencyclemap.org/transport/{z}/{x}/{y}.png"
                        (clj->js {:attribution "Map data &copy; [...]"
                                  :maxZoom 18
                                  }))
            leaflet)))

(defn home []
  (reagent/create-class {:reagent-render home-html
                         :component-did-mount home-did-mount}))

(defn ^:export main []
  (reagent/render [home]
                  (.getElementById js/document "app")))
