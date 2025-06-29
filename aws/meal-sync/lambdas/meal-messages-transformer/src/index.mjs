// @ts-check
import { DynamoDBClient } from "@aws-sdk/client-dynamodb";
import { PutCommand, GetCommand, DynamoDBDocumentClient } from "@aws-sdk/lib-dynamodb"

export const handler = async (event) => {
    if (!event.detail) {
        console.log("No detail in event. Skipping...");
        return;
    }
    const dynamoDbTable = new DynamoDBClient({});
    const dynamoDbDoc = DynamoDBDocumentClient.from(dynamoDbTable);
    const eventTable = event.detail.table;

    const table = await dynamoDbDoc.send(new GetCommand({
        TableName: process.env.DYNAMO_DB_TABLE,
        Key: {
            table: eventTable,
        },
    }));

    console.log(table.Item);

    const newTable = await dynamoDbDoc.send(new PutCommand({
        TableName: process.env.DYNAMO_DB_TABLE,
        Item: {
            table: eventTable,
            timestamp: new Date().getTime(),
            messages: [event.detail]
        },
    }));
};
