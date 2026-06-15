.PHONY: dev test

dev:
	clojure -M:dev:nrepl

lint:
	clojure -M:lint --lint src test

test: lint
	clojure -M:test

# N.B it's better to use an nrepl, this is here just incase you need it.
repl:
	clj -M -m cljs.main --repl-env node

build:
	clj -M -m cljs.main --target node --output-to edc.js -c edc.core

run:
	node ./target/edc.js
