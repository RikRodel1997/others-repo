import { App, Stack, StackProps } from "aws-cdk-lib";
import * as ec2 from "aws-cdk-lib/aws-ec2";
import * as ecs from "aws-cdk-lib/aws-ecs";
import * as ecs_patterns from "aws-cdk-lib/aws-ecs-patterns";

export class EcsConstructStack extends Stack {
  constructor(scope: App, id: string, props?: StackProps) {
    super(scope, id, props);

    const vpc = new ec2.Vpc(this, "MyVpc", {
      maxAzs: 3,
    });

    const cluster = new ecs.Cluster(this, "MyCluster", {
      vpc: vpc,
    });

    new ecs_patterns.ApplicationLoadBalancedFargateService(
      this,
      "MyFargateService",
      {
        cluster: cluster,
        cpu: 512,
        desiredCount: 3,
        taskImageOptions: {
          image: ecs.ContainerImage.fromRegistry(
            "public.ecr.aws/nginx/nginx:latest"
          ),
        },
        memoryLimitMiB: 2048,
        publicLoadBalancer: true,
      }
    );
  }
}
