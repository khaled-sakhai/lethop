"use client";

import React from 'react'
import styles from '../register/page.module.css';
import Divider from '@/common/divider';
import SocialForm from '@/components/forms/SocialForm';
import LoginForm from '@/components/forms/LoginForm';

export default function page() {
  return (
    <div className={styles.register}> 
    <section className={styles.intro}>
      <h2>تسحيل الدخول:</h2>
       
    </section>
 
   
    <LoginForm /><Divider dividerText={"او"} />  <SocialForm />
  </div>
  )
}
