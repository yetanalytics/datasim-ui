.PHONY: clean fig watch-sass

clean:
	rm -rf target *.log node_modules resources/public/css/style.css resources/public/css/style.css.map

fig:
	clj -A:fig

watch-sass:
	clj -A:watch-sass
