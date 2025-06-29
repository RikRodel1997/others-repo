export const handler = async (event) => {
    if (!event.timestamp) {
        event.timestamp = new Date().getTime();
    }
    if (!event.table) {
        event.table = "front-room-01";
    }
    event.course = "starter";
    console.log(event);
};
