(ns token)
(def rules [#"[a-z]"
            #"[a-zA-Z]+"
            #" "
            #"[0-9]+"])

(defn process [rules text]
  (if (empty? text)
    true
     (let [match (get-first-match rules text)]
     (if (seq match)
       (process rules (subs text (match 1)))
       false
       ))))

(defn get-first-match [rules text]
  (first (get-all-matches rules text)))

(defn get-all-matches [rules text]
  (reverse
    (sort-by (fn [range] (length range))
     (filter (fn [range] (and (seq range) (zero? (range 0))))
          (for [rule rules] (get-next-match rule text))))))

(defn get-next-match [regex text]
  (let [matcher (re-matcher regex text)]
    (if (.find matcher)
      [(.start matcher) (.end matcher)]
      []
      )))

(defn length [[start end]]
  (- end start))

; todo: use map instead of range vector? with built-in length fn?
; todo: return list of tokens (how to handle error? exception?)
