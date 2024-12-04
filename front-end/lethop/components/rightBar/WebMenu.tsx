import React, { type JSX } from 'react';
import{FireIcon,HomeIcon,BuildingLibraryIcon,BookOpenIcon,GlobeAltIcon,EnvelopeIcon,} from '@heroicons/react/24/outline';
import Link from 'next/link';

export default function WebMenu() {
    interface MainMenu {
        id: number;
        name: string;
        url: string;
        icon?:JSX.Element;
      }
    const lastItem:MainMenu[]=[      
          { id: 1, name: 'عن معافر', url: '/',icon:<GlobeAltIcon className='size-10 text-orange-4'/>},

        { id: 2, name: 'المدونة', url: '/',icon:<BookOpenIcon className='size-10 text-orange-4'/>},
        { id: 3, name: 'خصوصية المستخدم', url: '/',icon:<BuildingLibraryIcon className='size-10 text-orange-4'/>},
        { id: 4, name: 'الاتصال بنا', url: '/',icon:<EnvelopeIcon className='size-10 text-orange-4'/>},
      ]

  return (
    <>
    <h3> <span>مصادر:</span></h3>
    <ul className='flex flex-col gap-2'>
{lastItem.map((e,i)=>{
return (
<li key={e.id} className='text-dark  py-1'>
    <Link href={e.url} className='flex gap-2	'>
    {e.icon} {e.name}
    </Link>
    </li>
);
})}

    </ul>
    </>
 )
}
