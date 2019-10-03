# API

``curl --cacert selfsigned.pem  --user user:passw0rd https://va33tuvwbs020.wellpoint.com:8443/swagger-ui.html``

``curl --cacert selfsigned.pem  --user user:passw0rd -X GET --header 'Accept: application/json' 'https://va33tuvwbs020.wellpoint.com:8443/api/export/directoryVerification/clinics?largeGroupId=1'``

``curl --cacert selfsigned.pem  --user user:passw0rd -X GET --header 'Accept: application/json' 'https://va33tuvwbs020.wellpoint.com:8443/api/export/directoryVerification/providers?largeGroupId=1'``

curl --cacert selfsigned.pem --user user:passw0rd -X GET "https://localhost:8443/api/providers?startRow=0&numRows=20" -H "accept: */*" -H "Content-Type: application/json" 
curl -k --user user:passw0rd -X GET "https://localhost:8443/api/providers?startRow=0&numRows=20" -H "accept: */*" -H "Content-Type: application/json"

curl -k --user user:passw0rd -X GET "https://localhost:8443/api/providers/18" -H "accept: */*" 

curl -k --user user:passw0rd -X PATCH "https://localhost:8443/api/providers/18" -H "accept: */*" -H "Content-Type: application/json" -d "{\"Comments\":\"ola\"}"

curl -k --user user:passw0rd -X POST "https://localhost:8443/api/providers/18" -H "X-HTTP-Method-Override:PATCH" -H "accept: */*" -H "Content-Type: application/json" -d "{\"Comments\":\"ole\"}"

curl --cacert selfsigned.pem --user user:passw0rd -X GET "https://va33tuvwbs020.wellpoint.com:8443/api/providers/799996" -H "accept: */*" 
curl --cacert selfsigned.pem --user user:passw0rd -X POST "https://va33tuvwbs020.wellpoint.com:8443/api/providers/799996" -H "X-HTTP-Method-Override:PATCH" -H "accept: */*" -H "Content-Type: application/json" -d "{\"Comments\":\"ole\"}"


clinicId	dataOwner	address						display
763970		1			4955 S Durango Dr Ste 201	(702)933-7275
763971		1			5059 S Mccarran Blvd		(775)909-4356
763972		1			2964 W 4700 S Ste 103		(801)571-1231
763973		1			2200 E 4500 S Ste 250		(801)278-5822

so based on the data in the db, we get a 409 ( Item Already Exists ) with:
{
  "Address": {
    "Line1": "4955 S Durango Dr Ste 201"
  },
  "DataOwnerId": 1,
  "Phone": {
    "AreaCode": "702",
    "Number": "9337275"
  }
}
also with just the exact address:
{
  "Address": {
    "Line1": "4955 S Durango Dr Ste 201"
  },
  "DataOwnerId": 1,
  "Phone": { }
}
and with the partial address and exact phone:
{
  "Address": {
    "Line1": "4955 Durango Dr Ste"
  },
  "DataOwnerId": 1,
  "Phone": {
    "AreaCode": "702",
    "Number": "9337275"
 }
}

...but not with the partial address and partial phone, we get a 404 in  that case:
{
  "Address": {
    "Line1": "4955 Durango Dr Ste"
  },
  "DataOwnerId": 1,
  "Phone": {
    "AreaCode": "705",
    "Number": "9337275"
 }
}



