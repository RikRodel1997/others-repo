# meal-sync

This is an experimental use case where different message flows need to be synchronized and the chronological order of messages needs to be perserved. To highlight this, we'll take a contrived example of a restaurant process where three different message flows need to be synchronized before they can be shared with the kitchen staff. The kitchen staff can be assumed to have a screen where all the messages need to be displayed in a logical chronological order.

## Context
In this scenario, we're building a order delivery process for a restaurant that offers the customers a three-course menu where there's a `starter-course`, `main-course` and a `dessert-course`. We'll need to work with a generalized `meal-event` that gets outputted from Lambdas that are all at the end of a separate asynchronous flow. An example for the `meal-event` looks as follows:
```json
{
  "table": "front-room-01",
  "course": "starter",
  "timestamp": 1735689600000,
  "details": {},
}
```
The `"details"` block is not relevant for this experiment. We can simply assume that this block contains information about the actually dish that was ordered for the `"course"`.

## Architecture
