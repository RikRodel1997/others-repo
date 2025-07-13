# Getting Started with AWS Fargate

Material from the [Getting Started with AWS Fargate training on AWS Skill Builder](https://skillbuilder.aws/learn/6QS9CM1V7K/getting-started-with-aws-fargate/EDX6V7B5YR).
Glossary:

- EKS: Amazon Elastic Kubernetes Service (EKS)
- ECS: Amazon Elastic Container Service (ECS)
- ECR: Amazon Elastic Container Registry (ECR)
- Fargate: AWS Fargate

Touchpoints of the training are:

## Describe how Fargate works.

Fargate provides a service where the serverless operating model is brought into containerized workloads. This frees up developers time from managing infrastructure and instead developers can focus on building applications while Fargate manages underlying infrastructure.

## Familiarize yourself with the technical concepts of Fargate.

Fargate is a serverless compute layer for ECS or EKS. To become a basic user of Fargate, the following six concepts should be expanded upon.

- **Clusters**: ECS tasks can be grouped together in clusters. By using clusters applications can be isolated from each other so they don't use the same underlying architecture. By running tasks on Fargate, cluster resources are also managed.
- **Containers and images**: A container is a standardized unit of development that holds everything that is required for a piece of software to run. This includes the code of the software, system tools and system libraries. A container is created from a read-only template called an image. Images are commonly found as Dockerfiles that are plaintext files that specify all of the required components that should be available in the container.
- **Task definitions**: A task definition is a plaintext or JSON format file that describes one or more containers that collectively make up an application in ECS. A task definition can specify a maximum of 10 containers and acts as a blueprint that can take parameters. The parameters can apply to the operating system, which containers to use, which ports to open up and what data volumes should be used with the containers.
- **Tasks**: A task is the instantiation of a task definition into a cluster on ECS.
- **Services**: An ECS service can be used to run and maintain any number of tasks simultaneously in a cluster. If a task fails or stops for any reason, the ECS service scheduler launches a new instance based on a task definition, thereby maintaining a desired number of tasks.
- **Fargate architecture overview**:

## List typical use cases for Fargate.

- **Microservices and web applications**: Because Fargate can elastically scale up and down, it's ideal for running microservices and web applications.
- **Data processing**: Fargate is good for data processing because it can be part of a queue or workflow. When data processing starts, Fargate can scale up as needed to handle the workload. When the data processing is complete, Fargate can scale down to save on costs.
- **Legacy workloads**: Old Java applications that are not containerized can be run on Fargate to provide the benefits of elastic scaling.

## Specify what it would take to implement Fargate in a real-world scenario.

## Recognize the benefits of Fargate.

- **Less operational overhead**: Fargate eliminates operational overhead by providing a serverless environment for containerized workloads. There's less operational overhead that needs to be dedicated to deploying, hardening, patching and scaling containerized workloads. Fargate handles these aspects for developers.
- **Security by design**: Fargate has security by design by wrapping containerized workloads on ECS and EKS in a micro-virtual machine that provides a unique hypervisor-based barrier. Additionally, by making use of security groups, each containerized workload can be individually locked down on a network level.
- **Optimize spend**: Fargate shifts the sizing of resources from the virtual machine level to the container level. Container operators can independently control the vCPU and memory allocation on a task-by-task basis. This prevents overprovisioning and thus operational costs.

## Explain the cost structure of Fargate.

Fargate costs are a combination of the operating system used to run the containers and the compute resources (vCPU and memory) allocated to each ECS task or EKS pod. Additionally, if more storage than the default 20GB is required, this can be expanded to 200GB on a per-gigabyte fee.
