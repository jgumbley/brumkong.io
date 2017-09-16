define green
	@tput setaf 2; echo $1; tput sgr0;
endef

.PHONY: run
run:
	LEIN_FAST_TRAMPOLINE=y; lein figwheel
	$(call green,"[Upped]")

.PHONY: test
test:
	LEIN_FAST_TRAMPOLINE=y; lein doo phantom test once
	$(call green,"[Upped]")

.PHONY: less
less:
	lein less auto
	$(call green,"[Upped]")

.PHONY: clean
clean:
	lein clean

