(defproject eliza "1.0.0-SNAPSHOT"
  :description "Collaborative chatterbot"
  :jar-exclusions [#"\.git"] ; not necessary in a future release of leiningen
  :main eliza.core
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler eliza.server/app}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.8"]])
