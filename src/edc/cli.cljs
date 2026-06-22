(ns edc.cli
  "CLI entrypoint for edc."
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as string]
            [cljs.nodejs :as nodejs]
            ["fs" :as fs]
            ["os" :as os]
            ["path" :as path]))

(def cli-options
  [["-c" "--context PATH" "Path to configuration EDN file"
    :validate [#(.endsWith % ".edn") "Must be an .edn file"]]
   ["-v" "--version" "Print version and exit"]])

;; TODO need to resolve ~ to it's fully expanded before passing to readFileSync
(defn source-file [context-arg]
  (let [path (if context-arg
               context-arg
               (.. js/process -env -EDC))]
    (try
      (let [content (fs/readFileSync (expand-home path) "utf8")]
        content)
      (catch :default e
        (js/console.error "could not read file" (.-message e))))))

;; (.. js/process -env -HOME)

(defn ^:export -main [& args]
  (let [{:keys [options arguments errors summary] :as cli-input} (parse-opts args cli-options)]
    (cond
      ;; 1. Handle errors
      errors
      (do
        (println (string/join "\n" errors))
        (js/process.exit 1))

      ;; 2. Handle version flag
      (:version options)
      (do
        (println "edc version 0.1.0")
        (js/process.exit 0))

      ;; 3. Handle context path logic
      (:context options)
      (let [config-path (:context options)]
        (println "Loading context from:" config-path)
        ;; Your logic to read the EDN file goes here
        )

      ;; 4. Default case if no valid flags were provided
      :else
      (do
        (println "edc: Missing required arguments.")
        (println summary)
        (js/process.exit 1)))))

(set! *main-cli-fn* -main)
(nodejs/enable-util-print!)
