#!/usr/bin/env node
import * as cdk from "aws-cdk-lib";
import { EcsConstructStack } from "../lib/ecs_construct-stack";

const app = new cdk.App();
new EcsConstructStack(app, "EcsConstructStack");
