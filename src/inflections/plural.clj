(ns inflections.plural
  (:use [clojure.string  :only (blank?)]
        inflections.rules
        inflections.uncountable))

(def ^:dynamic *plural-rules* (atom []))

(defn plural!
  "Define rule(s) to map words from singular to plural.\n
  Examples: (plural! #\"$(?i)\" \"s\")
            (plural! #\"(ax|test)is$(?i)\" \"$1es\"
                     #\"(octop|vir)us$(?i)\" \"$1i\")"
  [& patterns-and-replacements]
  (doseq [rule (apply slurp-rules patterns-and-replacements)]
    (add-rule! *plural-rules* rule)))

(defn pluralize
  "Returns the plural of the given word.\n
  Example: (pluralize \"virus\") => \"virii\""
  [word]
  (if (or (blank? word) (uncountable? word))
    word
    (resolve-rules (rseq @*plural-rules*) word)))

(defn reset-plural-rules!
  "Resets the rules used to map from singular to plural."
  [] (reset-rules! *plural-rules*))

(defn init-plural-rules []
  (reset-plural-rules!)
  (plural!
   #"$(?i)" "s"
   #"s$(?i)" "s"
   #"(ax|test)is$(?i)" "$1es"
   #"(octop|vir)us$(?i)" "$1i"
   #"(alias|status)$(?i)" "$1es"
   #"(bu)s$(?i)" "$1ses"
   #"(buffal|tomat)o$(?i)" "$1oes"
   #"([ti])um$(?i)" "$1a"
   #"sis$(?i)" "ses"
   #"(?:([^f])fe|([lr])f)$(?i)" "$1$2ves"
   #"(hive)$(?i)" "$1s"
   #"([^aeiouy]|qu)y$(?i)" "$1ies"
   #"(x|ch|ss|sh)$(?i)" "$1es"
   #"(matr|vert|ind)(?:ix|ex)$(?i)" "$1ices"
   #"([m|l])ouse$(?i)" "$1ice"
   #"^(ox)$(?i)" "$1en"
   #"(quiz)$(?i)" "$1zes"))

