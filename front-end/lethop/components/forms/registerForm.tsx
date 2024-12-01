import React from "react";
import { toast } from "react-toastify";
import * as yup from "yup";
import DynamicForm from "@/common/DynamicForm";
import { countries } from "@/assets/countries";
import { useRouter } from "next/navigation";
import { useRegisterMutation } from "@/redux/features/authApiSlice";

type RegisterFormInputs = {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  country: string;
};
const registerSchema = yup.object().shape({
  email: yup
    .string()
    .required("Email is required")
    .email("Invalid email address"),
  password: yup
    .string()
    .required("Password is required")
    .min(6, "Password must be at least 6 characters"),
  firstName: yup.string().required("First name is required"),
  lastName: yup.string().required("Last name is required"),
  country: yup.string().required("Country is required"),
});

const RegisterForm: React.FC = () => {
  const router = useRouter();
  const [register, { isLoading }] = useRegisterMutation();

  const onSubmit = (data: RegisterFormInputs) => {
    const { email, password, firstName, lastName, country } = data;
    register({ email, password, firstName, lastName, country })
      .unwrap()
      .then((res) => {
        toast.success("تم ارسال بريد الكتروني لتفعيل حسابك1");
        router.push("/auth/login");
      })
      .catch((res) => {
        toast.error("حدث خطا اثناء التسجيل, الرجاء المحاولة مجددا");
      });
  };

  return (
    <DynamicForm<RegisterFormInputs>
      schema={registerSchema}
      onSubmit={onSubmit}
      onLoad={isLoading}
      buttonText="تسجيل"
      fields={[
        { name: "email", label: "الايميل", type: "email" },
        { name: "password", label: "كلمة السر", type: "password" },
        { name: "firstName", label: "الاسم", type: "text" },
        { name: "lastName", label: "اسم العائلة", type: "text" },
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
