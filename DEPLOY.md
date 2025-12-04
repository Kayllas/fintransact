# 🚀 Deployment Guide (Render.com)

This guide explains how to deploy **FinTransact** to [Render](https://render.com) using the Infrastructure as Code (Blueprint) file we created.

## Prerequisites

1.  **GitHub Account**: Your code must be on GitHub (which it is!).
2.  **Render Account**: Sign up at [render.com](https://render.com).
3.  **CloudAMQP Account (Optional but Recommended)**: Since Render doesn't have a free RabbitMQ, we recommend using [CloudAMQP](https://www.cloudamqp.com/) (Free Lemur Plan) for the message broker.

## Steps to Deploy

### 1. Create RabbitMQ (CloudAMQP)
*If you skip this, the backend might fail to start if it strictly requires RabbitMQ.*

1.  Go to [CloudAMQP](https://www.cloudamqp.com/) and create a free instance.
2.  Copy the **Host**, **User**, **Password**, and **Vhost** (if any).

### 2. Connect to Render

1.  Go to the [Render Dashboard](https://dashboard.render.com/).
2.  Click **New +** and select **Blueprint**.
3.  Connect your GitHub repository (`Kayllas/fintransact`).
4.  Render will automatically detect the `render.yaml` file.

### 3. Configure Environment Variables

Render will ask you to confirm the services. You might need to provide values for the RabbitMQ variables if you want it to work:

-   `SPRING_RABBITMQ_HOST`: (e.g., `sparrow.rmq.cloudamqp.com`)
-   `SPRING_RABBITMQ_USERNAME`: (Your CloudAMQP User)
-   `SPRING_RABBITMQ_PASSWORD`: (Your CloudAMQP Password)

*Note: The Database variables are handled automatically by Render!*

### 4. Deploy!

Click **Apply**. Render will:
1.  Provision a **PostgreSQL Database**.
2.  Build and deploy the **Backend** (Docker).
3.  Build and deploy the **Frontend** (Static Site).

### 5. Final Touch: Frontend API URL

Once the Backend is live, copy its URL (e.g., `https://fintransact-backend.onrender.com`).

You might need to update your Frontend to point to this URL.
-   **Option A (Rebuild)**: Update `environment.prod.ts` in your code with the new URL, push to GitHub, and Render will auto-redeploy.
-   **Option B (Runtime)**: If we configured runtime injection (advanced), you could set it in Render. For now, Option A is easiest.

## 🎉 Success!

Your application should now be live!
