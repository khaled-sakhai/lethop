import { User } from "@/type/user";
import { apiSlice } from "../services/apiSlice";

const authApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    retreiveUser: builder.query<User, void>({
      query: () => "api/v1/user/me",
    }),

    login: builder.mutation({
      query: ({ email, password }) => ({
        url: "auth/login",
        method: "POST",
        body: { email, password },
      }),
    }),

    register: builder.mutation({
      query: ({ email, password, firstName, lastName, country }) => ({
        url: "auth/signup",
        method: "POST",
        body: { email, password, firstName, lastName, country },
      }),
    }),
    userVerify: builder.query({
      query: (confirmationCode) => ({
        url: `auth/email/confirm?verify=${confirmationCode}`,
        method: "GET",
      }),
    }),

    logout: builder.mutation({
      query: () => ({
        url: "logout",
        method: "POST",
      }),
    }),
  }),
});

export const {
  useRetreiveUserQuery,
  useLoginMutation,
  useRegisterMutation,
  useLogoutMutation,
  useLazyUserVerifyQuery,
} = authApiSlice;
