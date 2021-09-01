(ns main.helper)
  
(defn new-color [color]
  (let [color-map {:red :yellow
                   :yellow :green
                   :green :red}]
    (get color-map color)))

(defn new-keyword [kw]
  (let [kw-map {:wololo :yolo
                :yolo   :bruh
                :bruh   :lala
                :lala   :wololo}]
    (get kw-map kw)))     
