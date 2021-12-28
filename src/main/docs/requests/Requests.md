# Create a Film

```bash 
curl --location --request POST 'http://localhost:8080/v1/films' \
--header 'Content-Type: application/json' \
--data-raw '{
"title" : "Star Wars: The Rise Of Skywalker",
"description": "In the riveting conclusion of the landmark Skywalker saga, new legends will be born-and the final battle for freedom is yet to come.",
"releaseYear": "2019",
"language": "English",
"rentalDuration": 7,
"rentalRate": "4.99",
"length": 165,
"replacementCost": "14.99",
"rating": "PG",
"specialFeaturesList": [ "SciFi", "Star Wars"],
"filmCategory": "Sci-Fi",
"actors":[
{
"firstName":"Daisy",
"lastName": "Ridley"
},
{
"firstName":"Carrie",
"lastName":"Fisher"
},
{
"firstName": "Oscar",
"lastName": "Isaac"
},
{
"firstName": "Adam",
"lastName": "Driver"
},
{
"firstName": "Mark",
"lastName": "Hamill"
},
{
"firstName": "John",
"lastName": "Boyega"
}
]
}'
```