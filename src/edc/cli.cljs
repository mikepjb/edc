(ns edc.cli
  "CLI entrypoint for edc."
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as string]))

(def cli-options
  [["-c" "--context PATH" "Path to configuration EDN file"
    :validate [#(.endsWith % ".edn") "Must be an .edn file"]]
   ["-v" "--version" "Print version and exit"]])

(defn ^:export -main [& args]
  (let [{:keys [options arguments errors summary] :as cli-input} (parse-opts args cli-options)]
    (println cli-input)
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

(-main)
