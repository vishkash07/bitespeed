
# Bitespeed Backend Task: Identity Reconciliation 
Java-based application that provides a streamlined solution for managing contact information. This system enables users to store and organize contact details, such as email addresses and phone numbers, and provides functionalities to efficiently handle primary and secondary contacts.






## API Info

Welcome to the heart of our application! Let's dive into how our APIs make managing contact information a breeze
### Create Customers

Endpoint to create new customers by sending a JSON payload.


- **URL**: `https://bitespeed-production-adf8.up.railway.app/identify`
- **Method**: POST
- **Content-Type**: application/json

⚠️ Warning: When testing our APIs, don't forget to leverage powerful tools like Postman, Thunder Client, or SoapUI. These tools provide an interactive and controlled environment for API testing, ensuring you get the most accurate results and avoid unintentional mishaps. 

**Request Body**

| Key          | Type     | Description                       |
| :------------ | :------- | :-------------------------------- |
| `Email`       | `string` | **Optional**. Email of Customer   |
| `PhoneNumber` | `string` | **Optional**. PhoneNumber of customer |

**Example Request**:

```http
POST /identify
Content-Type: application/json
{
  "Email": "john@example.com",
  "PhoneNumber": "1234567890"
}
```
**Example Response**:

```http
Content-Type: application/json
{
	"primaryContatctId": 11,
	"emails": ["john@example2.com"]
	"phoneNumbers": ["1234567890"]
	"secondaryContactIds": [27]	
}
```

**Example Request**:

```http
POST /identify
Content-Type: application/json
{
  "Email": "john@example2.com",
  "PhoneNumber": "1234567890"
}
```

**Example Response**:

```http
Content-Type: application/json
{
	"primaryContatctId": 11,
	"emails": ["john@example.com","john@example2.com"]
	"phoneNumbers": ["1234567890"]
	"secondaryContactIds": [27,28]	
}
```


## 🚀 About Me

Hello there! I'm Vishal, a passionate software engineer with a strong background in AWS and Java Spring Boot development. I'm on a journey to create scalable and efficient solutions that make a positive impact.

🛠️ **Tech Stack:** 
-
![AWS](https://img.shields.io/badge/AWS-Amazon%20Web%20Services-FF9900?style=flat-square&logo=amazon-aws)AWS (Amazon Web Services): I specialize in building cloud-based applications, leveraging AWS services to ensure high availability, scalability, and security. 

![Spring Boot Logo](https://img.shields.io/badge/Spring%20Boot-Backend%20Magic-green?style=flat-square&logo=spring) Java Spring Boot: My go-to framework for crafting robust and performant backend applications. I love building APIs that power modern web and mobile experiences.

🌱 **What I'm Currently Learning:**
I'm always looking to expand my skill set. Currently, I'm diving deeper into cloud to enhance my capabilities further.

📫 **Reach Out:**
Feel free to connect with me on [LinkedIn](https://www.linkedin.com/in/your-profile) to discuss software development, cloud computing, or any tech-related topic. Let's learn and grow together!

Keep coding, keep innovating, and keep pushing the boundaries of what's possible!
