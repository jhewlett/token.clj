(ns token)

(defn get-next-match [[regex token] text]
  (let [matcher (re-matcher regex text)]
    (if (.find matcher)
      (let [start (.start matcher)
            end (.end matcher)
            length (- end start)]
        {:start start :end end :length length :token token})
      {}
      )))

(defn matches-at-start-of-word? [match]
  (= 0 (:start match)))

(defn get-best-match [rules text]
  (->>
    (for [rule rules] (get-next-match rule text))
    reverse
    (filter matches-at-start-of-word?)
    (sort-by (fn [match] (:length match)))
    last))

(defn append-token-if-not-ignored [tokens this-token]
  (if (= this-token :IGNORE)
    tokens
    (conj tokens this-token)))

(defn get-tokens [rules text]
  (loop [rules rules
         text text
         tokens []]
    (if (empty? text)
      tokens
      (let [match (get-best-match rules text)]
        (if (seq match)
          (let [remaining-text (subs text (:end match))
                all-tokens (append-token-if-not-ignored tokens (:token match))]
            (recur rules remaining-text all-tokens))
          []
          )))))