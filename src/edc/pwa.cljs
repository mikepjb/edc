(ns edc.pwa
  "PWA, offline-first webapp for edc.")

;; Notice the ^:export metadata. This prevents advanced optimization
;; from renaming your -main function out of existence.
(defn ^:export -main [& args]
  (println "PWA Initialized!"))

(println "I want to be a PWA")
