export const EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,10}$/;
export const PASSWORD_REGEX = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d).{8,64}$/;
export const NAME_REGEX = /^[A-Za-z]{1,50}$/;
export const ADDRESS_REGEX = /^[a-zA-Z0-9\s,'-]{1,200}$/;
export const POSTCODE_REGEX = /^[0-9]{3,6}$/;
export const PHONE_REGEX = /^[0-9]{8,15}$/;
export const PRICE_REGEX = /^\d+$/;
export const TEXT_REGEX = /^[a-zA-Z0-9\s,'-]{1,255}$/;
