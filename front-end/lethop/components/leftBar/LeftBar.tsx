import React from 'react'
import styles from './leftBar.module.css'


interface barProps{
  show:Boolean,
  small:Boolean
}

export default function LeftBar({show,small}:barProps) {
  return (

        
       <aside className={`${(show && small) ? 'hidden': `${styles.leftBar} ${`bar`}`} `}>
        {/* Leftbar Content */}
        <h4 className='pb-4'>إعلانات:</h4>
        <div className='w-25 h-96  bg-sky-700	pb-4  mb-3  border place-content-center text-center'>250 X 600 </div>
        <div className='w-25 h-64 border bg-orange-4	 pb-4 mb-3 place-content-center text-center'>250 X 250 </div>

        <div className='w-25 h-64 border bg-gray pb-4 mb-3 place-content-center text-center'>250 X 250 </div>
        <div className='w-25 h-96  bg-gray-500	pb-4  mb-3  border place-content-center text-center'>250 X 600 </div>
        <div className='w-25 h-64 border bg-gray pb-4 mb-3 place-content-center text-center'>250 X 250 </div>
   
        <p>
          done!
        </p>
      </aside>



  )
}
