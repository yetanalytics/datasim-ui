.PHONY: clean fig watch-sass

clean:
	rm -rf target *.log node_modules resources/public/css/style.css resources/public/css/style.css.map gh-pages

fig:
	clj -A:fig

watch-sass:
	clj -A:watch-sass

build-prod:
	clj -A:build-prod

build-sass:
	clj -A:build-sass

gh-pages: build-prod build-sass
	mkdir -p gh-pages/cljs-out
	cp -r resources/public/* gh-pages/
	rm gh-pages/index.html
	mv gh-pages/prod-index.html gh-pages/index.html
	cp target/public/cljs-out/prod-main.js gh-pages/cljs-out/
