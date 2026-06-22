.PHONY: dev test

dev:
	clojure -M:dev

lint:
	clojure -M:lint --lint src test

test: lint
	clojure -M:test

clean:
	rm -rf ./target

# N.B it's better to use an nrepl and piggieback into cljs, this is here just
# incase you need no nonsense access.
repl:
	clj -M -m cljs.main --repl-env node

build-cli:
	clj -M -m cljs.main --target node --output-to ./target/cli/edc -c edc.cli
	chmod +x ./target/cli/edc

build-pwa:
	clj -M -m cljs.main \
		--target bundle \
		--optimizations advanced \
		--output-to ./target/pwa/edc.js \
		-c edc.pwa

run-cli:
	node ./target/cli/edc

run-pwa:
	mkdir -p ./target/pwa
	cp resources/public/* ./target/pwa
	python3 -m http.server -d ./target/pwa

install: build-cli
	cp ./target/cli/edc ~/.local/bin

