(ns user
  (:require [cljs.repl.node :as cljs-node]
            [cider.piggieback]))

;; (require 'cljs.repl.node)
(defn start-cljs []
  (cider.piggieback/cljs-repl (cljs-node/repl-env)))
