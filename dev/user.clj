(ns user
  (:require [cljs.repl.node :as cljs-node]
            [cider.piggieback]))

;; N.B use :Piggieback (start-cljs) to attach in vim-fireplace
(defn start-cljs []
  (cider.piggieback/cljs-repl (cljs-node/repl-env)))
