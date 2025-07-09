### Install Mountebank
`npm install -g mountebank`

### Start Mountebank
`mb`

# Upload imposter
`curl -X POST -H 'Content-Type: application/json' -d '@virtualServer\imposter.json' http://localhost:2525/imposters | jq`

### Delete imposter
`curl -X DELETE http://localhost:2525/imposters/5555 | jq`

### Get imposters 
`curl http://localhost:2525/imposters | jq`

### List requests made
`curl http://localhost:2525/imposters/5555 | jq ".[\"requests\"]"`