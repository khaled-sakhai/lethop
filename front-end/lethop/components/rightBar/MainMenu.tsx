import React from 'react'
import styles from './mainMenu.module.css'
import{FireIcon,HomeIcon,BuildingLibraryIcon,BookOpenIcon,GlobeAltIcon,EnvelopeIcon,} from '@heroicons/react/24/outline';
import Link from 'next/link';

export default function MainMenu() {
    interface MainMenu {
        id: number;
        name: string;
        url: string;
        icon?:JSX.Element;
      }
    
      const mainItems: MainMenu[] = [
        { id: 1, name: 'الرئيسية', url: '/',icon:<HomeIcon className='size-10 text-orange-4'/>},
        { id: 2, name: 'منشورات شائعة', url: '/' ,icon:<FireIcon className='size-10 text-orange-4'/>},
        { id: 3, name: 'المدونة', url: '/',icon:<BuildingLibraryIcon className='size-10 text-orange-4'/>},
      ];



  return (
    <section>
        <ul className=''>
{mainItems.map((e,i)=>{
  return (
    <li key={e.id} className='text-dark py-1'>
        <Link href={e.url} className='flex gap-x-1 '>
        {e.icon} {e.name}
        </Link>
        </li>
  );
})}

        </ul>
        
    </section>
  )
}
