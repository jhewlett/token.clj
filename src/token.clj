(ns token)

(def rules [[#"[a-z]+" :LOWER-ID]
            [#"[a-zA-Z]+" :UPPER-ID]
            [#"\s+" :SPACE]
            [#"[0-9]+" :NUM]
            [#"&" :IGNORE]])

(defn get-next-match [[regex token] text]
  (let [matcher (re-matcher regex text)]
    (if (.find matcher)
      (let [start (.start matcher)
            end (.end matcher)
            length (- end start)]
        {:start start :end end :length length :token token})
      {}
      )))

(defn get-best-match [rules text]
  (let [all-matches (for [rule rules] (get-next-match rule text))
        reversed-matches (reverse all-matches)
        matches-at-start (filter (fn [match] (= 0 (:start match))) reversed-matches)
        ordered-by-match-length (sort-by (fn [match] (:length match)) matches-at-start)]
    (last ordered-by-match-length)))

(defn append-token-if-not-ignored [tokens this-token]
  (if (= this-token :IGNORE)
    tokens
    (conj tokens this-token)))

(defn process-helper [rules text tokens]
  (if (empty? text)
    tokens
     (let [match (get-best-match rules text)]
     (if (seq match)
       (let [remaining-text (subs text (:end match))
             all-tokens (append-token-if-not-ignored tokens (:token match))]
         (process-helper rules remaining-text all-tokens))
       []
       ))))
  
(defn process [rules text]
  (process-helper rules text []))