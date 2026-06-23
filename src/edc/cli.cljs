(ns edc.cli
  "CLI entrypoint for edc."
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.edn :as edn]
            [clojure.string :as string]
            [clojure.pprint :as pp]
            [cljs.nodejs :as nodejs]
            ["fs" :as fs]
            ["os" :as os]
            ["path" :as path]
            [edc.core :as core]))

(def cli-options
  [["-c" "--context PATH" "Path to configuration EDN file"
    :validate [#(.endsWith % ".edn") "Must be an .edn file"]]
   ["-v" "--version" "Print version and exit"]])

(defn expand-path [file-path]
  (if (or (.startsWith file-path "~")
          (.startsWith file-path "$HOME"))
    (.join path (.homedir os) (.slice file-path 1))
    file-path))

(defn source-file [context-arg]
  (let [file-path (if context-arg
                    context-arg
                    (.. js/process -env -EDC))]
    (try
      (let [content (fs/readFileSync (expand-path file-path) "utf8")]
        content)
      (catch :default e
        (js/console.error "could not read file" (.-message e))))))

(defn update-file [file-path content]
  (fs/writeFileSync (expand-path file-path) content))

(defn ^:export -main [& args]
  (let [{:keys [options arguments errors summary] :as cli-input} (parse-opts args cli-options)
        [subcommand & sub-args] arguments]
    (cond
      errors ;; Handle errors
      (do
        (println (string/join "\n" errors))
        (js/process.exit 1))

      ;; Handle options --------------------------------------------
      (:version options)
      (do
        (println "edc version 0.1.0")
        (js/process.exit 0))

      (:context options)
      (let [config-path (or (:context options)
                            (.. js/process -env -EDC))
            config (edn/read-string (source-file config-path))]
       (println config) 
        )

      ;; Handle args/subcommands
      (= subcommand "add")
      (do
        (let [config-path (or (:context options)
                              (.. js/process -env -EDC))
              config (edn/read-string (source-file config-path))]
          (update-file config-path
                       (with-out-str (pp/pprint (core/add-task config "new task")))))
          (println "task added"))

      ;; 4. Default case if no valid flags were provided
      :else
      (pp/pprint (edn/read-string (source-file nil)))
      )))

(set! *main-cli-fn* -main)
(nodejs/enable-util-print!)
