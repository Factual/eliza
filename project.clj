(defproject eliza "1.0.0-SNAPSHOT"
  :description "Collaborative chatterbot"
  :jar-exclusions [#"\.git"] ; not necessary in a future release of leiningen
  :main eliza.core
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [funnyplaces "1.0-alpha-SNAPSHOT"]]
  :dev-dependencies [
	[swank-clojure "1.4.0-SNAPSHOT"]]
  )
