(ns main.components
  (:require [rum.core :as rum]
            [odoyle.rules :as o]
            [odoyle.rum :as orum]
            [main.helper :as h]))

(defn insert! [*session id attr->value]
  (swap! *session #(-> %
                       (o/insert id attr->value)
                       o/fire-rules)))
                       
        
                       
(def components
  (orum/ruleset
    {main-view
     [:then
      (let [*session (orum/prop)]
        [:div {:class "bg-gray-100"}
         (click-counter *session)
         [:div "below click-counter"]
         [:div "wololo"]
         (color-display *session)
         (facts-display *session)
         (keyword-modifier *session)])]
    
    
     click-counter
     [:what
      [::global ::clicks clicks]
      :then
      (let [*session (orum/prop)]
        [:a {:class "bg-gray-200 rounded px-4 py-2"
             :on-click #(insert! *session ::global {::clicks (inc clicks)})
             :href "#"}
         (str "Clicked " clicks " " (if (= 1 clicks) "time" "times"))])]
      
     color-display
     [:what
      [::global ::color color]
      :then
      (let [*session (orum/prop)]
        [:a {:class (str (when (= color :red) "bg-red-500 ")
                         (when (= color :yellow) "bg-yellow-500 ")
                         (when (= color :green) "bg-green-500 ")
                         "inline-flex py-2 px-4 w-40 justify-center rounded")
             :on-click #(insert! *session ::global {::color (h/new-color color)})
             :href "#"}
         (str color)])]
     
     facts-display
     [:what
      [::global ::color color]
      [::global ::clicks click]
      [::global ::test test]
      :then
      (let [*session (orum/prop)]
        [:div {:class "py-4 bg-gray-100"}
         (str (o/query-all @*session))])]
     
     keyword-modifier
     [:what
      [::global ::test keyword]
      :then
      (let [*session (orum/prop)]
        [:a {:class "bg-yellow-500 p-5 rounded"
             :on-click #(insert! *session ::global {::test (h/new-keyword keyword)})
             :href "#"}
         "modify keyword"])]}))
         
         
(def *session
  (-> (reduce o/add-rule (o/->session) components)
      (o/insert ::global {::clicks 0
                          ::color :red
                          ::test :wololo})
      o/fire-rules
      atom))
      

(defn start []
  (rum/mount (main-view *session) 
             (js/document.getElementById "app")))
  
