FROM mongo

COPY init.json /init.json

CMD mongoimport  -u $MONGO_USER -p $MONGO_PASSWORD --uri $MONGO_URI --collection users \
    --file /init.json --jsonArray --authenticationDatabase admin