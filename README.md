token.clj
=========

Port of token.js to Clojure. Simple regular expression-based lexer.

Usage
-----

```clojure
(def rules [[#"var" :VAR]
            [#"[a-zA-Z]+" :ID]
            [#"\s+" :IGNORE]
            [#"=" :ASSIGN]
            [#"[0-9]+" :NUM]])

(get-tokens rules "var sum = 123")
--------
[:VAR :ID :ASSIGN :NUM]
```

Note that :IGNORE is a special keyword to indicate that a token should be consumed and discarded.
