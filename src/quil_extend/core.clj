(ns quil-extend.core
  (:require [quil.core :as q])
  (:gen-class))

(defn create-anim [filenames]
  "Takes a vector of file names and returns an animation object"
  {:frames (map #(q/load-image %) filenames)
   :time 0
   :state :playing})

(defn play-anim [anim]
  (assoc anim :state :playing))

(defn pause-anim [anim]
  (assoc anim :state :paused))

(defn draw-anim [anim x y]
  "Draw an animation at a position"
  (let [frames (:frames anim)]
    (q/image (nth frames (mod (:time anim) (count frames)))
              x
              y)))

(defn update-anim [anim dt]
  "Make the animation play forward"
  (if (= (:state anim) :playing)
    (update-in anim [:time] #(+ dt %))
    anim))

(defn setup []
  (def test-anim (atom (create-anim (map #(str "Test" % ".png") (range 1 5))))))

(defn draw []
  (swap! test-anim update-anim 0.1)
  (q/background 250 220 150)
  (doseq [x (range 10 500 100)
          y (range 10 500 100)]
    (draw-anim @test-anim x y)))

(defn -main []
  (q/defsketch demo
             :title "Quil-Extend Demo"
             :setup setup
             :draw draw
             :size [500 500]))
