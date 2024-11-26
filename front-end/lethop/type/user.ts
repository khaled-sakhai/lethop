export type User={
    id: number,
    provider: string,
    email: string,
    needProfileUpdate: boolean,
    userName: string,
    notificationEmailed: boolean,
    postEmailed: boolean,
    roles:string[],
    savedCount: number,
    likedCount: number,
    commentCount: number,
    interests?:string[],
    active: boolean
}

export type userPhoto={
    id:number,
    url:string,
    fileName:string
}

export type userProfile={
     id: number,
      profileCreationDate: string,
      birthDate: string,
      country: string,
      city: string,
      summary: string,
}