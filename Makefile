.PHONY: clean fig

clean:
	rm -rf target *.log

fig:
	clj -A:fig
