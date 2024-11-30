import React from "react";
import * as yup from "yup";
import DynamicForm from "@/common/DynamicForm";

type LoginFormInputs = {
  email: string;
  password: string;
};
const LoginSchema = yup.object().shape({
  email: yup
    .string()
    .required("Email is required")
    .email("Invalid email address"),
  password: yup
    .string()
    .required("Password is required")
    .min(6, "Password must be at least 6 characters")
});

const LoginForm: React.FC = () => {
  const onSubmit = (data: LoginFormInputs) => {
    console.log("Login Data:", data);
  };

  return (
    <DynamicForm<LoginFormInputs>
      schema={LoginSchema}
      onSubmit={onSubmit}
      buttonText="تسجيل"
      fields={[
        { name: "email", label: "الايميل", type: "email" },
        { name: "password", label: "كلمة السر", type: "password" }
      ]}
    />
  );
};

export default LoginForm;
