.PHONY: dev-grant-chmod-make-scripts
dev-grant-chmod-make-scripts:  ## Grant permissions to run make-scripts locally
	chmod -R 755 make-scripts

.PHONY: dev-build
dev-build: ## Create docker image for your dev environment
	time make-scripts/dev-build

.PHONY: dev-run
dev-run: ## Run built image on dev container
	docker run -t -i --rm --name investment_container -p 8081:8081 investment_image