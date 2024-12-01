import React from "react";
import { SubmitHandler, Path, FieldValues, useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { AnyObjectSchema } from "yup";
import styles from "./dynamicForm.module.css";
import Spinner from "./Spinder";

// Define the props for the DynamicForm component
interface DynamicFormProps<T extends FieldValues> {
  schema: AnyObjectSchema;
  buttonText: string;
  onLoad: boolean;
  onSubmit: SubmitHandler<T>;
  fields: Array<{
    name: Path<T>; // Correctly typing `name` as `Path<T>`
    label: string;
    type: string;
    options?: { value: string; label: string }[];
  }>;
}

const DynamicForm = <T extends Record<string, any>>({
  schema,
  onLoad,
  onSubmit,
  buttonText,
  fields,
}: DynamicFormProps<T>) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<T>({
    resolver: yupResolver(schema),
  });

  return (
    <form onSubmit={handleSubmit(onSubmit)} className={styles.form}>
      {fields.map((field) => (
        <div key={field.name} className={styles.form_item}>
          <label className={styles.form_label}>{field.label}</label>
          {field.type === "select" ? (
            <select {...register(field.name)} className={styles.form_select}>
              <option value="">اختر {field.label.toLowerCase()}</option>
              {field.options?.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          ) : (
            <input
              className={styles.form_input}
              type={field.type}
              {...register(field.name)}
            />
          )}
          {errors[field.name] && (
            <p className={styles.form_error}>
              {(errors[field.name] as any)?.message}
            </p>
          )}
        </div>
      ))}
      <button className={styles.form_button} type="submit" disabled={onLoad}>
        {onLoad ? <Spinner sm /> : `${buttonText}`}
      </button>
    </form>
  );
};

export default DynamicForm;
