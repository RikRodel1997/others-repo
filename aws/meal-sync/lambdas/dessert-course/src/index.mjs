//@ts-check
import { EventBridgeClient, PutEventsCommand } from "@aws-sdk/client-eventbridge";

export const handler = async (event) => {
    const eventBridge = new EventBridgeClient({});
    if (!event.timestamp) {
        event.timestamp = new Date().getTime();
    }
    if (!event.table) {
        event.table = "front-room-01";
    }
    event.course = "dessert";

    const events = new PutEventsCommand({
        Entries: [
            {
                Detail: JSON.stringify(event),
                DetailType: event.course,
                Source: "dessert-course-lambda",
                EventBusName: "meal-bridge"
            }
        ]
    });

    const response = await eventBridge.send(events);
    console.log("response", response);

};
