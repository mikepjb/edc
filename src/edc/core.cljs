(ns edc.core)

(defn add-task [context summary]
  (update-in context [:tasks :self] conj {:task summary}))
