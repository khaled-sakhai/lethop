import React from "react";
import * as yup from "yup";
import DynamicForm from "@/common/DynamicForm";
import { countries } from "@/assets/countries";

interface RegisterFormInputs {
  username: string;
  email: string;
  password: string;
  re_password: string;
  firstName: string;
  lastName: string;
  country: string;
}
const registerSchema = yup.object().shape({
  username: yup
    .string()
    .required("Username is required")
    .min(3, "At least 3 characters"),
  email: yup
    .string()
    .required("Email is required")
    .email("Invalid email address"),
  password: yup
    .string()
    .required("Password is required")
    .min(6, "Password must be at least 6 characters"),
  re_password: yup
    .string()
    .oneOf([yup.ref("password")], "Passwords must match")
    .required("Please confirm your password"),
  firstName: yup.string().required("First name is required"),
  lastName: yup.string().required("Last name is required"),
  country: yup.string().required("Country is required"),
});

const RegisterForm: React.FC = () => {
  const onSubmit = (data: RegisterFormInputs) => {
    console.log("Register Data:", data);
  };

  return (
    <DynamicForm<RegisterFormInputs>
      schema={registerSchema}
      onSubmit={onSubmit}
      fields={[
        { name: "username", label: "اسم المستخدم", type: "text" },
        { name: "email", label: "الايميل", type: "email" },
        { name: "password", label: "كلمة السر", type: "password" },
        { name: "re_password", label: "تأكيد كلمة السر", type: "password" },
        { name: "firstName", label: "الاسم", type: "text" },
        { name: "lastName", label: "اللقب", type: "text" },
        {
          name: "country",
          label: "البلد",
          type: "select",
          options: countries,
        },
      ]}
    />
  );
};

export default RegisterForm;
