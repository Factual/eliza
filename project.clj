(defproject eliza "1.0.0-SNAPSHOT"
  :description "Collaborative chatterbot"
  :jar-exclusions [#"\.git"] ; not necessary in a future release of leiningen
  :main eliza.core
  :dependencies [[org.clojure/clojure "1.6.0"]])
