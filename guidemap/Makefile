define green
	@tput setaf 2; echo $1; tput sgr0;
endef

.PHONY: run
run:
	lein figwheel
	$(call green,"[Upped]")

.PHONY: test
test:
	lein test
	$(call green,"[Upped]")

.PHONY: less
less:
	lein less auto
	$(call green,"[Upped]")

.PHONY: clean
clean:
	lein clean

